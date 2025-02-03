import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const OAuthRedirectHandler = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token");

    if (token) {
      localStorage.setItem("token", token); // JWT 저장
      navigate("/profile"); // 로그인 후 이동할 페이지
    }
  }, []);

  return <div>로그인 처리 중...</div>;
};

export default OAuthRedirectHandler;