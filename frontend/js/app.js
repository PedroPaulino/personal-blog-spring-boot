const BASE_URL = "";

function getAuthHeader(){
    const credentials = localStorage.getItem("basicAuth");

    if (!credentials)
        return {};

    return { Authorization: `Basic ${credentials}`};
}

export async function getArticles() {
    const response = await fetch("/api/v1/public/articles");

    return response.json();
}

export async function getArticle(id) {
    const response = await fetch(`/api/v1/public/articles/${id}`);

    return response.json();
}