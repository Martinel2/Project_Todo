import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import Login from "./Login";
import Profile from "./Profile";
import TodoApp from "./todo/todoApp";
import OAuthRedirectHandler from "./components/OAuthRedirectHandler";
import axios from "axios";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const navigate = useNavigate();

  console.log(token);

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
    } else {
      localStorage.removeItem("token");
    }
  }, [token]);

  useEffect(() => {
    const interceptor = axios.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("token");
          setToken(null);
          navigate("/login");
        }
        return Promise.reject(error);
      }
    );

    return () => {
      axios.interceptors.response.eject(interceptor);
    };
  }, [navigate]);

  useEffect(() => {
    if (!token) {
      navigate("/login");  // 토큰이 없으면 로그인 페이지로 리다이렉트
    }
  }, [token, navigate]);

  return (
    <div>
      <Routes>
        <Route path="/" element={!token ? <Login setToken={setToken} /> : <TodoApp/>} />
        <Route path="/login" element={<Login setToken={setToken} />} />  {/* /login 경로 추가 */}
        <Route path="/oauth2/redirect" element={<OAuthRedirectHandler setToken={setToken} />} />
        <Route path="/profile" element={<Profile token={token} />} />
        <Route path="/todo" element={<TodoApp/>} />
      </Routes>
    </div>
  );
}

function AppWrapper() {
  return (
    <Router>
      <App />
    </Router>
  );
}

export default AppWrapper;
