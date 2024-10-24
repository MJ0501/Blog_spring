const deleteButton = document.getElementById('delete-btn');
if(deleteButton){
    deleteButton.addEventListener('click', (event) => {
        let id = document.getElementById('article-id').value;
        function success(){
             const successAlert = document.getElementById("successAlert");
             successAlert.innerText = "삭제가 완료했습니다.";
             successAlert.style.display = "inline-block";
             setTimeout(() => {
                successAlert.classList.add("show");
             }, 70);
             setTimeout(() => {
                 successAlert.classList.add("hide");
             }, 1800);
             setTimeout(() => {
                 successAlert.style.display = "none";
                 successAlert.classList.remove("hide");
                 location.replace("/articles");
             }, 2500);

        }
        function fail(){
            alert('삭제를 실패했습니다.');
            location.replace("/articles");
        }
        httpRequest("DELETE", "/api/articles/"+id, null, success, fail);
    });
}
//수정 : title은 꼭 입력, 내용은 없어도 상관없음
const modifyButton = document.getElementById('modify-btn');
if (modifyButton) {
    modifyButton.addEventListener('click', (event) => {
        const titleElement = document.getElementById('title');
        const contentElement = document.getElementById('content');
        const contentValue = contentElement ? contentElement.value : "";
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        if (!titleElement) {
            console.error("title 요소가 존재하지 않습니다.");
            return;
        }

        if (!titleElement.value.trim()) {
            const alertMessage = document.getElementById("alertMessage");
            alertMessage.innerText = "제목을 입력해주세요.";
            const alertModal = new bootstrap.Modal(document.getElementById('alertModal'));
            alertModal.show();
            return;
        }
        const body = JSON.stringify({
            title: titleElement.value,
            content: contentValue,
        });
        function success() {
             const successAlert = document.getElementById("successAlert");
             successAlert.innerText = "수정이 완료되었습니다.";
             successAlert.style.display = "inline-block";
             setTimeout(() => {
                 successAlert.classList.add("show");
             }, 70);
             setTimeout(() => {
                 successAlert.classList.add("hide");
             }, 1800);
             setTimeout(() => {
                 successAlert.style.display = "none";
                 successAlert.classList.remove("hide");
                 location.replace("/articles/"+id);
             }, 2500);

        }
        function fail() {
            alert('수정이 실패했습니다.');
            location.replace("/articles/" + id);
        }
        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}
const createButton = document.getElementById("create-btn");
if (createButton) {
    createButton.addEventListener('click', event => {
        const titleElement = document.getElementById("title");
        const contentElement = document.getElementById("content");

        if (!titleElement.value.trim() || !contentElement.value.trim()) {
            const alertMessage = document.getElementById("alertMessage");
            alertMessage.innerText = "제목과 내용을 모두 입력해주세요.";
            const alertModal = new bootstrap.Modal(document.getElementById('alertModal'));
            alertModal.show();
            return;
        }

        const body = JSON.stringify({
            title: titleElement.value,
            content: contentElement.value,
        });

        function success() {
         const successAlert = document.getElementById("successAlert");
             successAlert.innerText = "등록이 완료되었습니다.";
             successAlert.style.display = "inline-block";
             setTimeout(() => {
                 successAlert.classList.add("show");
             }, 70);
             setTimeout(() => {
                 successAlert.classList.add("hide");
             }, 1800);
             setTimeout(() => {
                 successAlert.style.display = "none";
                 successAlert.classList.remove("hide");
                 location.replace("/articles");
             }, 2500);
        }

        function fail() {
            alert('등록이 실패했습니다.');
            location.replace("/articles");
        }
        httpRequest("POST", "/api/articles", body, success, fail);
    });
}
const cancelButton = document.getElementById('cancel-btn');
if (cancelButton) {
    cancelButton.addEventListener('click', () => {
        window.history.back();
    });
}
function getCookie(key){
    var result = null;
    var cookie = document.cookie.split(";");
    cookie.some(function (item) {
        item = item.replace(" ","");
        var dic = item.split("=");

        if(key == dic[0]){
            result = dic[1];
            return result;
        }
    });
    return result;
}
function httpRequest(method, url, body, success, fail){
    fetch(url,{
        method: method,
        headers: {
            Authorization: "Bearer "+localStorage.getItem("access_token"),
            "Content-Type": "application/json",
        },
        body: body,
    }).then((response)=>{
        if(response.status === 200 || response.status === 201){
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if(response.status === 401 && refresh_token){
            fetch("/api/token", {
                method: "POST",
                headers: {
                    Authorization: "Bearer "+localStorage.getItem("access_token"),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                refreshToken: getCookie("refresh_token"),
                }),
            }).then((res)=>{
                if(res.ok){
                    return res.json();
                }
            }).then((result)=>{
                localStorage.setItem("access_token",result.accessToken);
                httpRequest(method, url, body, success, fail);
            }).catch((error) => fail());
        }else{
            return fail();
        }
    });
}
const logoutButton = document.getElementById("logout-btn");

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        function success() {
            localStorage.removeItem("access_token");

            deleteCookie("refresh_token");
            location.replace("/login");
        }
        function fail() {
            alert("로그아웃 실패했습니다.");
        }

        httpRequest('DELETE','/api/refresh-token', null, success, fail);
    });
}

function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}