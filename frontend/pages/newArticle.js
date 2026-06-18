import { createArticle }
from "../js/api.js";

export function renderNewArticle() {

    document.getElementById("app").innerHTML = `
        <div class="card">

            <h2>Novo Artigo</h2>

            <form id="articleForm">

                <input
                    name="title"
                    placeholder="Título">

                <input
                    name="category"
                    placeholder="Categoria">

                <input
                    name="tags"
                    placeholder="tag1,tag2">

                <textarea
                    name="content">
                </textarea>

                <button
                  class="btn btn-primary">
                    Publicar
                </button>

            </form>

        </div>
    `;

    document
        .getElementById("articleForm")
        .addEventListener("submit",
        async e => {

            e.preventDefault();

            const form =
                new FormData(e.target);

            const dataObj = {};

            form.forEach((value, key) => {
                dataObj[key] = value;
            });
            
            const jsonData = JSON.stringify(dataObj);

            await createArticle(jsonData);
        });
}