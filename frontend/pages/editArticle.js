import {
    getArticle,
    updateArticle
}
from "../js/api.js";

export async function renderEditArticle(id) {

    const article =
        await getArticle(id);

    document.getElementById("app").innerHTML = `
        <div class="card">

            <h2>Editar Artigo</h2>

            <form id="editForm">

                <input
                    name="title"
                    value="${article.title}">

                <input
                    name="category"
                    value="${article.category}">

                <input
                    name="tags"
                    value="${article.tags}">

                <textarea
                    name="content">${article.content}</textarea>

                <button
                  class="btn btn-primary">
                    Salvar
                </button>

            </form>

        </div>
    `;

    document
        .getElementById("editForm")
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

            await updateArticle(id, jsonData);

        });
}