export function ensureAuth(){
    
    if (localStorage.getItem("basicAuth"))
        return;

    const username = prompt("User");
    const password = prompt("Password");

    const encoded = btoa(`${username}:${password}`);

    localStorage.setItem("basicAuth", encoded);
}