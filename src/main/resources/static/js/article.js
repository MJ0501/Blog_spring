const deleteButton = document.getElementById('delete-btn');
if(deleteButton){
    deleteButton.addEventListener('click', (event) => {
        let id = document.getElementById('article-id').value;
        function success(){
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }
        function fail(){
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
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
        // title은 꼮 입력하기!
        if (!titleElement.value.trim()) {
            const alertMessage = document.getElementById("alertMessage");
            alertMessage.innerText = "제목을 입력해주세요.";
            const alertModal = new bootstrap.Modal(document.getElementById('alertModal'));
            alertModal.show();
            return;
        }
        const body = JSON.stringify({
            title: titleElement.value,
            content: contentValue,  // 내용은 비어 있어도 전송
        });
        function success() {
            alert('수정이 완료되었습니다.');
            location.replace("/articles/" + id);
        }
        function fail() {
            alert('수정이 실패했습니다.');
            location.replace("/articles/" + id);
        }
        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}
// 글 생성시에는 제목과 내용 필수입력
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
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("POST", "/api/articles", body, success, fail);
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
const logoutButton = document.getElementById('logout-btn');

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        function success() {
            // 로컬 스토리지에 저장된 액세스 토큰을 삭제
            localStorage.removeItem('access_token');

            // 쿠키에 저장된 리프레시 토큰을 삭제
            deleteCookie('refresh_token');
            location.replace('/login');
        }
        function fail() {
            alert('로그아웃 실패했습니다.');
        }

        httpRequest('DELETE','/api/refresh-token', null, success, fail);
    });
}

function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}