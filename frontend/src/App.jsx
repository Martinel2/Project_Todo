import { useState, useEffect } from "react";
import Login from "./Login";
import Profile from "./Profile";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || null);

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token); // 토큰을 localStorage에 저장
    } else {
      localStorage.removeItem("token"); // 토큰이 없으면 삭제
    }
  }, [token]); // token이 바뀔 때마다 실행

  return (
    <div>
      <h1>JWT 로그인</h1>
      {!token ? <Login setToken={setToken} /> : <Profile token={token} />}
    </div>
  );
}

export default App;
