import { ensureAuth } from "../js/auth.js";
import {
    getAdminArticles,
    deleteArticle
} from "../js/api.js";

export async function renderAdmin() {

    ensureAuth();

    const articles = await getAdminArticles();

    document.getElementById("app").innerHTML = `

<section class="admin-container">

    <div class="admin-header">

        <h1>Artigos</h1>

        <a
            href="/new"
            class="btn btn-primary">

            + Novo Artigo

        </a>

    </div>

    <div class="admin-list">

        ${articles.map(article => `

        <article class="admin-article">

            <h2>${article.title}</h2>

            <p>
                ${article.content.substring(0, 120)}...
            </p>

            <div class="admin-actions">

                <a
                    href="/edit/${article.id}"
                    class="btn btn-secondary">

                    Editar

                </a>

                <button
                    class="btn btn-danger delete-btn"
                    data-id="${article.id}">

                    Excluir

                </button>

            </div>

        </article>

        `).join("")}

    </div>

</section>

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