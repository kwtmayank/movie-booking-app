
import Header from './components/Header';
import Home from './components/Home';
import Login from './components/Login';
import { useAppSelector } from './hooks/redux';

function App() {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);

  return (
    <>
      <Header />
      {isLoggedIn ? <Home /> : <Login />}
    </>
  );
}

export default App;
