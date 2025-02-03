import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login";
import Profile from "./Profile";
import OAuthRedirectHandler from "./components/OAuthRedirectHandler";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || null);

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token); // 토큰을 localStorage에 저장
    } else {
      localStorage.removeItem("token"); // 토큰이 없으면 삭제
    }
  }, [token]);

  return (
    <Router>
      <div>
        <h1>JWT 로그인</h1>
        <Routes>
          <Route path="/" element={!token ? <Login setToken={setToken} /> : <Profile token={token} />} />
          <Route path="/oauth2/redirect" element={<OAuthRedirectHandler setToken={setToken} />} />
          <Route path="/profile" element={<Profile token={token} />} />
        </Routes>
      </div>
    </Router>
  );
}
export default App;
