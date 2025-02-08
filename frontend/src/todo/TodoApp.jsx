import { useState, useEffect } from "react";
import "../styles/Todo.css";

function TodoApp() {
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState("");

  // 📝 할 일 목록 불러오기
  useEffect(() => {
    fetch("http://localhost:8080/api/todos")
      .then((res) => res.json())
      .then((data) => setTodos(data));
  }, []);

  // ➕ 할 일 추가하기
  const addTodo = () => {
    if (!newTodo.trim()) return; // 빈 값 방지

    fetch("http://localhost:8080/api/todos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title: newTodo }),
    })
      .then((res) => res.json())
      .then((todo) => {
        setTodos([...todos, todo]); // 상태 업데이트
        setNewTodo(""); // 입력창 초기화
      });
  };

  return (
    <div className="todo-container">
      <h1>Todo 리스트</h1>
      <a href="/profile">내  정보</a>
      <input
        className="todo-input"
        type="text"
        value={newTodo}
        onChange={(e) => setNewTodo(e.target.value)}
        placeholder="할 일을 입력하세요"
      />
      <button
        className="todo-input" 
        onClick={addTodo}>추가</button>
      <ul>
        {todos.map((todo) => (
          <li key={todo.id}>{todo.title}</li>
        ))}
      </ul>
    </div>
  );
}

export default TodoApp;
