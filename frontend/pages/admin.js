import { ensureAuth } from "../js/auth.js";
import {
    getAdminArticles,
    deleteArticle
} from "../js/api.js";

export async function renderAdmin() {

    ensureAuth();

    const articles = await getAdminArticles();

    document.getElementById("app").innerHTML = `
        <div style="margin-bottom:20px;">
            <a href="/new" class="btn btn-primary">
                Novo Artigo
            </a>
        </div>

        ${
            articles.map(article => `
                <div class="card">

                    <h2>${article.title}</h2>

                    <br/>

                    <a
                      href="/edit/${article.id}"
                      class="btn btn-warning">
                        EDIT
                    </a>

                    <button
                      class="btn btn-danger delete-btn"
                      data-id="${article.id}">
                        DELETE
                    </button>

                </div>
            `).join("")
        }
    `;

    document.querySelectorAll(".delete-btn")
        .forEach(button => {

            button.addEventListener("click",
                async () => {

                    const id =
                        button.dataset.id;

                    const confirmed =
                        confirm(
                          "Deseja realmente excluir?"
                        );

                    if(!confirmed)
                        return;

                    await deleteArticle(id);
                });
        });
}