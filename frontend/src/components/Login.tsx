import { useState } from 'react';
import axios from 'axios';
import { CredentialResponse, GoogleLogin } from '@react-oauth/google';
import { useAppDispatch } from '../hooks/redux';
import { loginSuccess } from '../store/authSlice';

type UserProfile = {
  id: number;
  email: string;
  name: string;
  pictureUrl: string | null;
  dateOfBirth: string | null;
  address: string | null;
  city: string | null;
  phoneNumber: string | null;
};

type AuthResponse = {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  user: UserProfile;
};

function Login() {
  const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;
  const dispatch = useAppDispatch();
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleGoogleSuccess = async (credentialResponse: CredentialResponse) => {
    const idToken = credentialResponse.credential;

    if (!idToken) {
      setErrorMessage('Google login did not return an ID token.');
      return;
    }

    try {
      setIsLoggingIn(true);
      setErrorMessage('');

      const response = await axios.post<AuthResponse>(
        '/api/auth/google/login',
        { idToken },
        { withCredentials: true }
      );

      dispatch(loginSuccess(response.data));
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const backendMessage = error.response?.data?.message;
        const status = error.response?.status;
        setErrorMessage(
          backendMessage
            ? `Login failed (${status}): ${backendMessage}`
            : `Login failed${status ? ` (${status})` : ''}. Please try again.`
        );
      } else {
        setErrorMessage('Login failed. Please try again.');
      }
    } finally {
      setIsLoggingIn(false);
    }
  };

  const handleGoogleError = () => {
    setErrorMessage('Google sign-in failed. Please try again.');
  };

  return (
    <main className="container d-flex min-vh-100 align-items-center justify-content-center py-5">
      <div className="card w-100 border-0 shadow-sm" style={{ maxWidth: '420px' }}>
        <div className="card-body p-4 p-md-5 text-center">
          <h2 className="h4 mb-2">Login</h2>
          <p className="text-secondary mb-4">Continue to book your favorite movies.</p>
          {!googleClientId && (
            <p className="alert alert-warning mb-3 py-2">
              Missing <code>VITE_GOOGLE_CLIENT_ID</code> in your environment.
            </p>
          )}
          {googleClientId && (
            <div className="d-flex justify-content-center">
              <GoogleLogin
                onSuccess={handleGoogleSuccess}
                onError={handleGoogleError}
                useOneTap={false}
              />
            </div>
          )}
          {isLoggingIn && <p className="mt-3 mb-0 text-secondary">Signing you in...</p>}
          {errorMessage && <p className="mt-3 mb-0 text-danger">{errorMessage}</p>}
        </div>
      </div>
    </main>
  );
}

export default Login;
