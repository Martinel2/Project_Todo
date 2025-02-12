import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import Login from "./Login";
import Profile from "./Profile";
import TodoApp from "./todo/todoApp";
import OAuthRedirectHandler from "./components/OAuthRedirectHandler";
import axios from "axios";
import {jwtDecode} from "jwt-decode";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  // token 상태가 변경될 때마다 localStorage에 저장하거나 삭제하는 작업
  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
    } else {
      localStorage.removeItem("token");
    }
  }, [token]);

  // 401 에러 처리: 토큰이 만료된 경우 로그아웃 처리
  useEffect(() => {
    const interceptor = axios.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("token");
          setIsAuthenticated(false);
        }
        return Promise.reject(error);
      }
    );

    return () => {
      axios.interceptors.response.eject(interceptor);
    };
  }, []);

  // token이 있을 경우 사용자 정보 디코딩 후 저장
  useEffect(() => {
    if (!token) {
      setIsAuthenticated(false);
    } else{    
        axios
        .get("http://localhost:8080/api/auth/me", {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((res) => {
          setUser(res.data);
          setIsAuthenticated(true);
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            // 401 오류가 발생
            setIsAuthenticated(false);
          }
        });
    }
    
  }, [token]);

  return (
    <div>
      <Routes>
        <Route
          path="/"
          element={isAuthenticated ? <TodoApp user={user} /> : <Login setToken={setToken} />}
        />
        <Route path="/login" element={!isAuthenticated ? <Login setToken={setToken} /> : <TodoApp user={user}/>} />
        <Route path="/oauth2/redirect" element={<OAuthRedirectHandler setToken={setToken} />} />
        <Route path="/profile" element={isAuthenticated? <Profile user={user}/> : <Login setToken={setToken} />} />
        <Route path="/todo" element={isAuthenticated? <TodoApp user={user} /> : <Login setToken={setToken} />} />
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
