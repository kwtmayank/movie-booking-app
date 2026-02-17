import { useAppSelector } from '../hooks/redux';

function Home() {
  const userName = useAppSelector((state) => state.auth.userName);

  return (
    <main className="container py-5">
      <div className="card border-0 shadow-sm">
        <div className="card-body p-4 p-md-5">
          <h2 className="h4 mb-2">Home</h2>
          <p className="text-secondary mb-4">Welcome, {userName}.</p>
          <p className="mb-4">You are logged in.</p>
        </div>
      </div>
    </main>
  );
}

export default Home;
