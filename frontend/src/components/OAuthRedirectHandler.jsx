import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function OAuthRedirectHandler({ setToken }) {
  const navigate = useNavigate();

  useEffect(() => {
    // URL에서 토큰을 가져오기
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (token) {
      // 토큰을 state에 설정
      setToken(token);
      // 로컬 스토리지에 토큰 저장
      localStorage.setItem("token", token);
      console.log("토큰 설정됨:", token);
      // 프로필 페이지로 리다이렉트
      navigate("/");
    } 
  }, [navigate, setToken]);

  return <div>로그인 중...</div>;
}

export default OAuthRedirectHandler;
