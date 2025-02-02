import { useEffect, useState } from "react";

function Profile({ token }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (!token) return;

    fetch("http://localhost:8080/api/auth/me", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setUser(data));
  }, [token]);

  return (
    <div>
      <h2>프로필</h2>
      {user ? <p>환영합니다, {user.username}님!</p> : <p>로그인이 필요합니다.</p>}
    </div>
  );
}

export default Profile;
