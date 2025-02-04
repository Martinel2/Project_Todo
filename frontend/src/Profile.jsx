import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Profile({ token }) {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      navigate("/login");  // 토큰이 없으면 로그인 페이지로 리다이렉트
      return;
    }

    axios
      .get("http://localhost:8080/api/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setUser(res.data))
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          // 401 오류가 발생하면 로그인 페이지로 리다이렉트
          navigate("/login");
        }
      });
  }, [token, navigate]);

  return (
    <div>
      <h2>대시보드</h2>
      {user ? (
        <>
          <p>환영합니다, {user.username}님!</p>
          <p>로그인한 플랫폼: {user.provider}</p>
          {user.provider === "google" && <img src="path_to_google_icon" alt="Google" />}
          {user.provider === "kakao" && <img src="path_to_kakao_icon" alt="Kakao" />}
          {user.provider === "naver" && <img src="path_to_naver_icon" alt="Naver" />}
          {user.provider === null && <img src="path_to_todo_icon" alt="Todo" />}
        </>
      ) : (
        <p>로그인이 필요합니다.</p>
      )}
    </div>
  );
}

export default Profile;
