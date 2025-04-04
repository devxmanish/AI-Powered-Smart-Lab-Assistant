import logo from './logo.svg';
import './App.css';
import MarkdownDisplay from './components/MarkdownDisplay';
import FacultyDashboard from './components/FacultyDashboard';

function App() {
  return (
    <div className="App">
      {<FacultyDashboard/>}
      {/* <h2 className="text-xl font-bold mb-4">AI Powered Lab Assistant</h2>
      <div className="generated-content">
        <MarkdownDisplay/>
        
      </div> */}
    </div>
  );
}

export default App;
