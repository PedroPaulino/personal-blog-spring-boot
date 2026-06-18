import { getArticle } from "../js/api.js";

export async function renderArticle(id) {

    const article = await getArticle(id);
    
    document.getElementById("app").innerHTML = `
        <div class="card">

            <h1>${article.title}</h1>

            <p class="meta">
                ${new Date(article.createdAt)
                    .toLocaleDateString()}
            </p>

            <br>

            <p>${article.content}</p>

            <br>

            <p>
                <strong>Categoria:</strong>
                ${article.category}
            </p>

            <p>
                <strong>Tags:</strong>
                ${article.tags}
            </p>

        </div>
    `;

}