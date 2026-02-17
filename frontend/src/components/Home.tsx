import { useEffect, useState } from 'react';
import axios from 'axios';
import { useAppSelector } from '../hooks/redux';

const CITY_VISUALS: Record<string, string> = {
  Mumbai: '🌊',
  Delhi: '🕌',
  Bengaluru: '🌳',
  Hyderabad: '🏰',
  Chennai: '🏖️',
  Pune: '🏞️',
  Kolkata: '🌉'
};

function Home() {
  const userName = useAppSelector((state) => state.auth.userName);
  const [cities, setCities] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState('');

  const fetchCities = async () => {
    try {
      setIsLoading(true);
      setErrorMessage('');
      const response = await axios.get<string[]>('/api/cities');
      setCities(response.data);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const backendMessage = error.response?.data?.message;
        const status = error.response?.status;
        setErrorMessage(
          backendMessage
            ? `Failed to load cities (${status}): ${backendMessage}`
            : `Failed to load cities${status ? ` (${status})` : ''}.`
        );
      } else {
        setErrorMessage('Failed to load cities.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    void fetchCities();
  }, []);

  return (
    <main className="home-screen py-5">
      <div className="container">
        <div className="city-panel p-4 p-md-5">
          <p className="home-eyebrow mb-2">Now Showing Across India</p>
          <h2 className="home-title mb-2">Welcome, {userName}</h2>
          <p className="home-subtitle mb-4">
            Pick a city to explore movie schedules and start your booking.
          </p>

          {isLoading && <p className="home-state mb-0">Loading cities...</p>}

          {!isLoading && errorMessage && (
            <div className="home-state">
              <p className="mb-2 text-danger">{errorMessage}</p>
              <button type="button" className="btn btn-sm btn-outline-dark" onClick={() => void fetchCities()}>
                Retry
              </button>
            </div>
          )}

          {!isLoading && !errorMessage && (
            <div className="row g-3 g-md-4">
              {cities.map((city, index) => (
                <div className="col-12 col-sm-6 col-lg-4" key={city}>
                  <article className="city-tile" style={{ animationDelay: `${index * 80}ms` }}>
                    <div className="city-tile-media" aria-hidden="true">
                      {CITY_VISUALS[city] ?? '🎬'}
                    </div>
                    <h3 className="city-tile-name mb-0">{city}</h3>
                  </article>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </main>
  );
}

export default Home;
