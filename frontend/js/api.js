const BASE_URL = "http://localhost:8080";

function getAuthHeader(){
    const credentials = localStorage.getItem("basicAuth");

    if (!credentials)
        return {};

    return { Authorization: `Basic ${credentials}`};
}

export async function getArticles() {
    const response = await fetch(`${BASE_URL}/api/v1/public/articles`);

    return response.json();
}

export async function getArticle(id) {
    const response = await fetch(`${BASE_URL}/api/v1/public/articles/${id}`);

    return response.json();
}

export async function getAdminArticles() {

    const response =
        await fetch(`${BASE_URL}/api/v1/admin/articles`, {
            headers: getAuthHeader()
        });

    return response.json();
}

export async function createArticle(article) {

    const response =
        await fetch(`${BASE_URL}/api/v1/admin/articles`, {
            method:"POST",
            headers:{
                ...getAuthHeader(),
                "Content-Type":"application/json"
            },
            body:article
        });

    return response.json();
}

export async function updateArticle(id, article) {

    const response =
        await fetch(`${BASE_URL}/api/v1/admin/articles/${id}`, {
            method:"PUT",
            headers:{
                ...getAuthHeader(),
                "Content-Type":"application/json"
            },
            body:article
        });
        
    return response.json();
}

export async function deleteArticle(id) {

    return fetch(`${BASE_URL}/api/v1/admin/articles/${id}`, {
        method:"DELETE",
        headers:getAuthHeader()
    });
}

