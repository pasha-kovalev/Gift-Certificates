export function getAuthHeader() {
    console.log('Bearer ' + getToken());
    return (getUser() && localStorage.getItem('token')) ? 'Bearer ' + getToken() : "";
}

export function getToken() {
    checkExpiration();
    return JSON.parse(localStorage.getItem("token"));
}

export function getUser() {
    return JSON.parse(localStorage.getItem("user"));
}

export function getAdminAuthorities() {
    return "ROLE_ADMIN";
}

export function clearAuthData() {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    window.dispatchEvent(new Event("storage"));
}

export function checkExpiration() {
    if (!getUser() || getUser().exp < new Date().getTime) {
        clearAuthData();
    }
}

export function hasAuthority(role, user) {
    if (!user || !user.authorities) {
        user = getUser();
    }
    return user.authorities ? user.authorities.includes(role) : false;
}

export function setToken(token) {
    localStorage.setItem("token", JSON.stringify(token));
}

export function setUser(user) {
    localStorage.setItem("user", JSON.stringify(user));
    window.dispatchEvent(new Event("storage"));
}


