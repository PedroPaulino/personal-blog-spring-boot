import { renderAdmin } from "../pages/admin.js";
import { renderArticle } from "../pages/article.js";
import { renderEditArticle } from "../pages/editArticle.js";
import { renderHome } from "../pages/home.js";
import { renderNewArticle } from "../pages/newArticle.js";

export async function router() {
    const path = location.pathname;
    console.log(path);
    if(path === "/" || path === "/home"){
        return renderHome();
    }

    if(path.startsWith("/article/")){
        const id = path.split("/")[2];
        return renderArticle(id);
    }

    if(path === "/admin"){
        return renderAdmin();
    }

    if(path === "/new")
        return renderNewArticle();

    if(path.startsWith("/edit/")){
        const id = path.split("/")[2];
        return renderEditArticle(id);
    }
}                                            