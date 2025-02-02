import { useState } from "react";
import Login from "./Login";
import Profile from "./Profile";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || null);

  return (
    <div>
      <h1>JWT 로그인</h1>
      {!token ? <Login setToken={setToken} /> : <Profile token={token} />}
    </div>
  );
}

export default App;
