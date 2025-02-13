import { useState, useEffect } from "react";
import axios from "axios";
import "../styles/Todo.css";

function TodoApp({ user }) {
  const [todos, setTodos] = useState([]);  // Ensure todos is always an array
  const [task, setTask] = useState("");
  const [loading, setLoading] = useState(false);

  const { email, provider } = user;

  const getTodos = async () => {
    setLoading(true);  // Start loading
    try {
      const response = await axios.get(`http://localhost:8080/api/todos/user?email=${email}&provider=${provider}`);
      if (Array.isArray(response.data)) {
        setTodos(response.data);
      } else {
        console.error("Expected array, but got:", response.data);
        setTodos([]);
      }
    } catch (error) {
      console.error("Error fetching todos:", error);
      setTodos([]);
    } finally {
      setLoading(false);  // End loading
    }
  };
  //console.log(todos);
  const createTodo = async () => {
    if (!task.trim()) {
      console.error("Task cannot be empty");
      return;
    }

    try {
      const newTodo = { email, provider, task };
      const response = await axios.post("http://localhost:8080/api/todos/create", newTodo);

      // 응답 확인 (디버깅용)
      console.log("Todo created:", response.data);

      // 새로 생성된 todo를 todos 목록에 추가
      setTodos((prevTodos) => [...prevTodos, response.data]);
      setTask("");  // task 초기화
    } catch (error) {
      console.error("Error creating todo:", error);
      if (error.response) {
        // 서버로부터의 응답이 있을 경우, 응답 내용 로그 출력
        console.error("Server responded with:", error.response.data);
      }
    }
  };

  const deleteTodo = async (id) => {  // id를 받아서 해당 todo 삭제
    try {
      const response = await axios.post("http://localhost:8080/api/todos/delete", { id });  // id만 보내기
  
      // 응답 확인 (디버깅용)
      console.log("Todo deleted:", response.data);
  
      // 삭제된 todo를 todos 목록에서 제거
      setTodos((prevTodos) => prevTodos.filter(todo => todo.id !== id));  // 해당 id를 제외한 todos로 갱신
    } catch (error) {
      console.error("Error deleting todo:", error);
      if (error.response) {
        // 서버로부터의 응답이 있을 경우, 응답 내용 로그 출력
        console.error("Server responded with:", error.response.data);
      }
    }
  };
  

  useEffect(() => {
    if (user && email && provider) {
      getTodos();
    }
  }, [user]);

  return (
    <div className="todo-container">
      <h1>Todo 리스트</h1>
      <a href="/profile">내 정보</a>
      <input
        className="todo-input"
        type="text"
        value={task}
        onChange={(e) => setTask(e.target.value)}
        placeholder="할 일을 입력하세요"
      />
      <button className="todo-input" onClick={createTodo}>추가</button>

      <h2>Todo List</h2>
      {loading ? (
        <p>Loading...</p>  // Show loading message
      ) : (
        <ul>
          {Array.isArray(todos) && todos.length > 0 ? (
            todos.map((todo) => (
              <li key={todo.id}>
                {todo.title}
                <button className="todo-input" onClick={() => deleteTodo(todo.id)}>삭제</button>  {/* 삭제 버튼에 id 전달 */}
              </li>
            ))
          ) : (
            <li>No todos found</li>
          )}
        </ul>
      )}
    </div>
  );
}

export default TodoApp;
