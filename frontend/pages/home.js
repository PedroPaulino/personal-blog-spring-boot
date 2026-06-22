import { getArticles } from "../js/api.js";

export async function renderHome() {
    const app = document.getElementById("app");
    const articles = await getArticles();

    app.innerHTML =
    `

        <section class="article-list">

            ${articles.map(article => `

            <article class="article-preview">

                <div class="article-category">
                    ${article.category}
                </div>

                <h2>

                    <a href="/article/${article.id}">
                        ${article.title}
                    </a>

                </h2>

                <p class="article-excerpt">

                    ${article.content.substring(0, 180)}...

                </p>

                <div class="article-meta">

                    ${new Date(article.createdAt)
                        .toLocaleDateString("en-US", {
                            month:"long",
                            day:"numeric",
                            year:"numeric"
                        })}

                </div>

            </article>

            `).join("")}

        </section>

    `;
}