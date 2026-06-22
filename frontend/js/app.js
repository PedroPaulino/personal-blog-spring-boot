import { router } from "./router.js"


window.addEventListener("DOMContentLoaded", () => {
    router();
})

window.addEventListener("popstate", () => {
    router();
});

document.addEventListener("click", e => {
    const link = e.target.closest("a");
    if(!link) return;
    const href = link.getAttribute("href");
    if(href.startsWith("/")) {
        e.preventDefault();
        history.pushState({}, "", href);
        router();
    }
});
