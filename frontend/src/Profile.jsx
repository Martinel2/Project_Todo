function Profile({ user }) {
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
