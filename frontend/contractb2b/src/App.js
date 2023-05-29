import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Join from "./pages/Join";
import Client from "./pages/Client";

function App() {
  return (
    <div className="root">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/join" element={<Join />} />
        <Route path="/client" element={<Client />} />
      </Routes>
    </div>
  );
}

export default App;
