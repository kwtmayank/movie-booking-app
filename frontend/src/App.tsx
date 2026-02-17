import { useEffect, useState } from 'react';
import Header from './components/Header';
import Home from './components/Home';
import Login from './components/Login';
import Profile from './components/Profile';
import { useAppSelector } from './hooks/redux';

function App() {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const [activePage, setActivePage] = useState<'home' | 'profile'>('home');

  useEffect(() => {
    if (!isLoggedIn) {
      setActivePage('home');
    }
  }, [isLoggedIn]);

  return (
    <>
      <Header onProfileClick={() => setActivePage('profile')} />
      {isLoggedIn ? (
        activePage === 'profile' ? <Profile onBack={() => setActivePage('home')} /> : <Home />
      ) : (
        <Login />
      )}
    </>
  );
}

export default App;
