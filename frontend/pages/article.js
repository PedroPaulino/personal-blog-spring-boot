import { getArticle } from "../js/api.js";

export async function renderArticle(id) {

    console.log(document.getElementById("app"))

    const article = await getArticle(id);

    const content = article.content
        .split("\n")
        .filter(p => p.trim())
        .map(p => `<p>${p}</p>`)
        .join("");
    
    const tags =
        typeof article.tags === "string"
            ? article.tags.split(",")
            : article.tags || [];
    
    document.getElementById("app").innerHTML = 
    `

        <article class="article-page">

            <div class="article-category">
                ${article.category}
            </div>

            <h1>${article.title}</h1>

            <p class="meta">

                ${new Date(article.createdAt)
                    .toLocaleDateString("en-US", {
                        month:"long",
                        day:"numeric",
                        year:"numeric"
                    })}

            </p>

            <div class="article-content">

                ${content}

            </div>

            <div class="article-info">

                <strong>Tags</strong>

                <div class="tags">

                    ${tags.map(tag => `
                        <span class="tag">
                            ${tag.trim()}
                        </span>
                    `).join("")}

                </div>

            </div>

        </article>

    `;

}