package com.tiny.security.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.tiny.common.enums.DataScopeEnum;
import com.tiny.security.context.LoginUser;
import com.tiny.security.utils.LoginUserUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限拦截器
 */
public class DataScopeInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 检查是否需要忽略
        if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
            return;
        }

        // 获取数据权限参数
        DataScopeParam dataScopeParam = DataScopeContext.get();
        if (dataScopeParam == null) {
            return;
        }

        // 获取当前登录用户
        LoginUser loginUser;
        try {
            loginUser = LoginUserUtil.getLoginUser();
        } catch (Exception e) {
            return;
        }

        if (loginUser == null) {
            return;
        }

        // 超级管理员不限制数据权限
        if (loginUser.isSuperAdmin()) {
            return;
        }

        // 获取数据范围
        String dataScope = loginUser.getDataScope();
        if (StrUtil.isBlank(dataScope)) {
            return;
        }

        // 构建数据权限SQL条件
        String dataScopeSql = buildDataScopeSql(loginUser, dataScopeParam);
        if (StrUtil.isBlank(dataScopeSql)) {
            return;
        }

        // 解析SQL并添加数据权限条件
        try {
            String originalSql = boundSql.getSql();
            Statement statement = CCJSqlParserUtil.parse(originalSql);

            if (statement instanceof Select) {
                Select select = (Select) statement;
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

                Expression where = plainSelect.getWhere();
                Expression dataScopeExpression = CCJSqlParserUtil.parseCondExpression(dataScopeSql);

                if (where == null) {
                    plainSelect.setWhere(dataScopeExpression);
                } else {
                    plainSelect.setWhere(new AndExpression(where, dataScopeExpression));
                }

                PluginUtils.mpBoundSql(boundSql).sql(select.toString());
            }
        } catch (Exception e) {
            // SQL解析失败，不添加数据权限条件
        }
    }

    /**
     * 构建数据权限SQL条件
     */
    private String buildDataScopeSql(LoginUser loginUser, DataScopeParam param) {
        String dataScope = loginUser.getDataScope();
        DataScopeEnum dataScopeEnum = DataScopeEnum.getByCode(dataScope);
        if (dataScopeEnum == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder();
        String deptAlias = StrUtil.isNotBlank(param.getDeptAlias()) ? param.getDeptAlias() + "." : "";
        String userAlias = StrUtil.isNotBlank(param.getUserAlias()) ? param.getUserAlias() + "." : "";
        String deptIdColumn = StrUtil.isNotBlank(param.getDeptIdColumn()) ? param.getDeptIdColumn() : "dept_id";
        String userIdColumn = StrUtil.isNotBlank(param.getUserIdColumn()) ? param.getUserIdColumn() : "user_id";

        switch (dataScopeEnum) {
            case ALL:
                // 全部数据权限，不添加限制
                return null;

            case CUSTOM:
                // 自定义数据权限
                Set<Long> deptIds = loginUser.getDataScopeDeptIds();
                if (CollUtil.isEmpty(deptIds)) {
                    // 没有配置自定义数据权限，只能查看自己的数据
                    sql.append(userAlias).append(userIdColumn).append(" = ").append(loginUser.getUserId());
                } else {
                    String deptIdsStr = deptIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                    sql.append(deptAlias).append(deptIdColumn).append(" IN (").append(deptIdsStr).append(")");
                }
                break;

            case DEPT:
                // 本部门数据权限
                if (loginUser.getDeptId() != null) {
                    sql.append(deptAlias).append(deptIdColumn).append(" = ").append(loginUser.getDeptId());
                } else {
                    sql.append(userAlias).append(userIdColumn).append(" = ").append(loginUser.getUserId());
                }
                break;

            case DEPT_AND_CHILD:
                // 本部门及以下数据权限（需要查询子部门，这里使用ancestors字段）
                if (loginUser.getDeptId() != null) {
                    // 使用FIND_IN_SET或LIKE来匹配ancestors字段
                    sql.append("(").append(deptAlias).append(deptIdColumn).append(" = ").append(loginUser.getDeptId());
                    sql.append(" OR ").append(deptAlias).append(deptIdColumn).append(" IN (");
                    sql.append("SELECT dept_id FROM sys_dept WHERE FIND_IN_SET(").append(loginUser.getDeptId()).append(", ancestors)");
                    sql.append("))");
                } else {
                    sql.append(userAlias).append(userIdColumn).append(" = ").append(loginUser.getUserId());
                }
                break;

            case SELF:
                // 仅本人数据权限
                sql.append(userAlias).append(userIdColumn).append(" = ").append(loginUser.getUserId());
                break;

            default:
                return null;
        }

        return sql.toString();
    }
}
