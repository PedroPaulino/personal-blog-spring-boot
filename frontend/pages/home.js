import { getArticles } from "../js/api.js";

export async function renderHome() {
    const app = document.getElementById("app");
    const articles = await getArticles();

    app.innerHTML =
        articles.map(article => `
            <div class="card">
                <h2>
                    <a href="/article/${article.id}">
                        ${article.title}
                    </a>
                </h2>

                <p class="meta">
                    ${new Date(article.createdAt)
                        .toLocaleDateString("en-US", {
                            month:"long",
                            day:"numeric",
                            year:"numeric"
                            }
                        )
                    }
                </p>
            </div>
            `).join("");
}