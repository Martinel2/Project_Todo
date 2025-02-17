import { useState,useEffect } from "react";
import axios from "axios";
import "../styles/Todo.css";

function TodoApp({ user }) {
  const { email, provider } = user;
  const [notes, setNotes] = useState([]);
  const [editingNote, setEditingNote] = useState(null);
  
  
  const addNote = async () => {
    const newNote = { email, provider, content: "" };
    try {
      const response = await axios.post("http://localhost:8080/api/todos/create", newNote);
      setNotes([...notes, response.data]);
      setEditingNote(response.data.id);
    } catch (error) {
      console.error("Error creating note:", error);
    }
  };

  const getTodos = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/todos/user?email=${email}&provider=${provider}`);
      if (Array.isArray(response.data)) {
        setNotes(response.data);
      } else {
        console.error("Expected array, but got:", response.data);
        setNotes([]);
      }
    } catch (error) {
      console.error("Error fetching todos:", error);
      setNotes([]);
    }
  };

  const updateNote = async (id, content) => {
    const updateDto = {id, content};
    if (content.trim() === "" || content === null) {
      deleteNote(id);
      return;
    }
    try {
      await axios.post("http://localhost:8080/api/todos/update", updateDto);
      setNotes(notes.map(note => (note.id === id ? { ...note, content } : note)));
    } catch (error) {
      console.error("Error updating note:", error);
    }
  };

  const deleteNote = async (id) => {
    try {
      await axios.post("http://localhost:8080/api/todos/delete", { id });
      setNotes(notes.filter(note => note.id !== id));
    } catch (error) {
      console.error("Error deleting note:", error);
    }
  };

  useEffect(() => {
    if (user && email && provider) {
      getTodos();
    }
  }, [user]);

  //console.log(notes);

  return (
    <div className="notion-container" onClick={() => addNote()}>
      <h1>Notion-style Notes</h1>
      <div className="notes">
        {notes.map((note) => (
          <div
            key={note.id}
            className="note"
            contentEditable={editingNote === note.id}
            suppressContentEditableWarning
            onClick={(e) => {
              e.stopPropagation();
              setEditingNote(note.id);
            }}
            onBlur={(e) => {
              updateNote(note.id, e.target.innerText);
              setEditingNote(null);
            }}
          >
            {note.content}
          </div>
        ))}
      </div>
    </div>
  );
}

export default TodoApp;
