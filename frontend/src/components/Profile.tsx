import { FormEvent, useState } from 'react';
import axios from 'axios';
import { useAppSelector } from '../hooks/redux';
import { useAppDispatch } from '../hooks/redux';
import { updateProfileDetails } from '../store/authSlice';

type ProfileProps = {
  onBack: () => void;
};

function Profile({ onBack }: ProfileProps) {
  const dispatch = useAppDispatch();
  const userId = useAppSelector((state) => state.auth.userId);
  const userName = useAppSelector((state) => state.auth.userName);
  const userEmail = useAppSelector((state) => state.auth.userEmail);
  const userPictureUrl = useAppSelector((state) => state.auth.userPictureUrl);
  const savedDob = useAppSelector((state) => state.auth.userDob);
  const savedAddress = useAppSelector((state) => state.auth.userAddress);
  const savedCity = useAppSelector((state) => state.auth.userCity);
  const savedPhone = useAppSelector((state) => state.auth.userPhone);
  const [dob, setDob] = useState(savedDob);
  const [address, setAddress] = useState(savedAddress);
  const [city, setCity] = useState(savedCity);
  const [phone, setPhone] = useState(savedPhone);
  const [isSaving, setIsSaving] = useState(false);
  const [saveMessage, setSaveMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleSave = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!userId) {
      setErrorMessage('Unable to update profile right now. Please login again.');
      return;
    }

    try {
      setIsSaving(true);
      setErrorMessage('');
      setSaveMessage('');

      const response = await axios.put(`/api/profile/users/${userId}`, {
        dateOfBirth: dob || null,
        address: address || null,
        city: city || null,
        phoneNumber: phone || null
      });

      dispatch(
        updateProfileDetails({
          dob: response.data?.dateOfBirth || '',
          address: response.data?.address || '',
          city: response.data?.city || '',
          phone: response.data?.phoneNumber || ''
        })
      );

      setSaveMessage('Profile details saved to database.');
      setTimeout(() => setSaveMessage(''), 2200);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const backendMessage = error.response?.data?.message;
        const status = error.response?.status;
        setErrorMessage(
          backendMessage
            ? `Save failed (${status}): ${backendMessage}`
            : `Save failed${status ? ` (${status})` : ''}. Please try again.`
        );
      } else {
        setErrorMessage('Save failed. Please try again.');
      }
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <main className="profile-screen py-5">
      <div className="container">
        <section className="profile-panel p-4 p-md-5">
          <div className="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
            <div>
              <p className="home-eyebrow mb-2">Your Account</p>
              <h2 className="home-title mb-1">Profile</h2>
              <p className="home-subtitle mb-0">Manage your movie booking identity details.</p>
            </div>
            <button type="button" className="btn btn-outline-dark btn-sm profile-back-btn" onClick={onBack}>
              Back To Home
            </button>
          </div>

          <div className="profile-card">
            <div className="profile-avatar-wrap">
              {userPictureUrl ? (
                <img src={userPictureUrl} alt={userName} className="profile-avatar" />
              ) : (
                <div className="profile-avatar profile-avatar-fallback">{userName.slice(0, 1).toUpperCase()}</div>
              )}
            </div>

            <div className="profile-details">
              <h3 className="profile-name mb-2">{userName}</h3>
              <p className="profile-email mb-0">{userEmail}</p>
            </div>
          </div>

          <form className="profile-form mt-4" onSubmit={handleSave}>
            <div className="row g-3">
              <div className="col-12 col-md-6">
                <label className="form-label profile-label" htmlFor="profile-dob">
                  Date of Birth
                </label>
                <input
                  id="profile-dob"
                  type="date"
                  className="form-control profile-input"
                  value={dob}
                  onChange={(e) => setDob(e.target.value)}
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label profile-label" htmlFor="profile-phone">
                  Phone Number
                </label>
                <input
                  id="profile-phone"
                  type="tel"
                  placeholder="e.g. +91 98765 43210"
                  className="form-control profile-input"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label profile-label" htmlFor="profile-city">
                  City
                </label>
                <input
                  id="profile-city"
                  type="text"
                  className="form-control profile-input"
                  value={city}
                  onChange={(e) => setCity(e.target.value)}
                />
              </div>
              <div className="col-12">
                <label className="form-label profile-label" htmlFor="profile-address">
                  Address
                </label>
                <textarea
                  id="profile-address"
                  rows={3}
                  className="form-control profile-input"
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                />
              </div>
            </div>

            <div className="d-flex align-items-center gap-3 mt-3">
              <button type="submit" className="btn btn-dark btn-sm px-4 profile-save-btn" disabled={isSaving}>
                {isSaving ? 'Saving...' : 'Save Details'}
              </button>
              {saveMessage && <span className="profile-save-message">{saveMessage}</span>}
            </div>
            {errorMessage && <p className="profile-error-message mt-2 mb-0">{errorMessage}</p>}
          </form>
        </section>
      </div>
    </main>
  );
}

export default Profile;
