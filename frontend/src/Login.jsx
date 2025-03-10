import { useState } from "react";

function Login({ setToken }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    const data = await response.json();
    if (response.ok) {
      localStorage.setItem("token", data.token);
      setToken(data.token);
    } else {
      alert(data.error);
    }
  };

  return (
    <div>
      <h2>로그인</h2>
      <form onSubmit={handleLogin}>
        <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)} />
        <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} />
        <button type="submit">로그인</button>
      </form>
      <a href="http://localhost:8080/oauth2/authorization/google" className="btn btn-sm btn-success active" role="button">Google Login</a><br/>
      <a href="http://localhost:8080/oauth2/authorization/naver" className="btn btn-sm btn-success active" role="button">Naver Login</a><br/>
      <a href="http://localhost:8080/oauth2/authorization/kakao" className="btn btn-third active" role="button">Kakao Login</a>

    </div>
  );
}

export default Login;
