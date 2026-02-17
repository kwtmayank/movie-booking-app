import Dropdown from 'react-bootstrap/Dropdown';
import { useAppSelector } from '../hooks/redux';
import { useAppDispatch } from '../hooks/redux';
import { logout } from '../store/authSlice';

function Header() {
  const dispatch = useAppDispatch();
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const userPictureUrl = useAppSelector((state) => state.auth.userPictureUrl);

  const handleLogout = () => {
    dispatch(logout());
  };

  return (
    <header className="bg-black py-3">
      <div className="container d-flex align-items-center justify-content-between">
        <h1 className="m-0 text-white fs-4">Movie Booking App</h1>
        {isLoggedIn && userPictureUrl && (
          <Dropdown align="end">
            <Dropdown.Toggle
              as="button"
              type="button"
              className="btn p-0 border-0 bg-transparent"
              aria-label="Open user menu"
            >
              <img
                src={userPictureUrl}
                alt="User profile"
                className="rounded-circle"
                style={{ width: '40px', height: '40px', objectFit: 'cover' }}
              />
            </Dropdown.Toggle>
            <Dropdown.Menu>
              <Dropdown.Item>Profile</Dropdown.Item>
              <Dropdown.Item onClick={handleLogout} className="text-danger">
                Logout
              </Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        )}
      </div>
    </header>
  );
}

export default Header;
