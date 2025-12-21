/**
 * @see https://umijs.org/docs/max/access#access
 */
export default function access(
  initialState: { currentUser?: API.CurrentUser } | undefined,
) {
  const { currentUser } = initialState ?? {};
  return {
    canAdmin: currentUser && currentUser.access === 'admin',
    hasRoles: (roles: string[]) => {
      if (!currentUser?.roles) return false;
      return roles.some((role) => currentUser.roles?.includes(role));
    },
    hasPermission: (permission: string) => {
      if (!currentUser?.permissions) return false;
      return currentUser.permissions.includes(permission);
    },
    hasPermissions: (permissions: string[]) => {
      if (!currentUser?.permissions) return false;
      return permissions.some((p) => currentUser.permissions?.includes(p));
    },
  };
}
