import { Routes, Route } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Login from "./pages/Login";
import Join from "./pages/Join";
import Client from "./pages/Client";
import Contractor from "./pages/Contractor";
import Citizen from "./pages/Citizen";

// 라우팅 컴포넌트
function App() {
  return (
    <div className="root">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/join" element={<Join />} />
        <Route path="/client" element={<Client />} />
        <Route path="/contractor" element={<Contractor />} />
        <Route path="/citizen" element={<Citizen />} />
      </Routes>
    </div>
  );
}

export default App;
