import { renderHome } from "../pages/home";

export async function router() {
    const path = location.pathname;

    if(path === "/" || path === "/home"){
        return renderHome();
    }
}