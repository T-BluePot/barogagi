import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";

function App() {
  const [count, setCount] = useState(0);

  return (
    <div className="min-h-screen flex items-center justify-center">
      <h1 className="text-4xl font-bold text-main-500">
        Hello, Tailwind CSS 4.0!
      </h1>
      <div className="text-title-01 text-main-dark bg-main-disable font-bold p-4 rounded-card">
        테스트
      </div>
    </div>
  );
}

export default App;
