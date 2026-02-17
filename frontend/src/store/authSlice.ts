import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type AuthState = {
  isLoggedIn: boolean;
  userId: number | null;
  token: string | null;
  userName: string;
  userEmail: string;
  userPictureUrl: string | null;
  userDob: string;
  userAddress: string;
  userCity: string;
  userPhone: string;
};

const initialToken = localStorage.getItem('auth_token');
const initialUserId = localStorage.getItem('auth_user_id');
const initialUserName = localStorage.getItem('auth_user_name');
const initialUserEmail = localStorage.getItem('auth_user_email');
const initialPictureUrl = localStorage.getItem('auth_user_picture_url');
const initialUserDob = localStorage.getItem('auth_user_dob');
const initialUserAddress = localStorage.getItem('auth_user_address');
const initialUserCity = localStorage.getItem('auth_user_city');
const initialUserPhone = localStorage.getItem('auth_user_phone');

const initialState: AuthState = {
  isLoggedIn: Boolean(initialToken),
  userId: initialUserId ? Number(initialUserId) : null,
  token: initialToken,
  userName: initialUserName || 'User',
  userEmail: initialUserEmail || 'Not available',
  userPictureUrl: initialPictureUrl || null,
  userDob: initialUserDob || '',
  userAddress: initialUserAddress || '',
  userCity: initialUserCity || '',
  userPhone: initialUserPhone || ''
};

type LoginPayload = {
  accessToken: string;
  user: {
    id: number;
    name: string;
    email: string;
    pictureUrl?: string | null;
    dateOfBirth?: string | null;
    address?: string | null;
    city?: string | null;
    phoneNumber?: string | null;
  };
};

type ProfileDetailsPayload = {
  dob: string;
  address: string;
  city: string;
  phone: string;
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess: (state, action: PayloadAction<LoginPayload>) => {
      const { accessToken, user } = action.payload;
      state.isLoggedIn = true;
      state.userId = user.id;
      state.token = accessToken;
      state.userName = user.name;
      state.userEmail = user.email;
      state.userPictureUrl = user.pictureUrl || null;
      state.userDob = user.dateOfBirth || '';
      state.userAddress = user.address || '';
      state.userCity = user.city || '';
      state.userPhone = user.phoneNumber || '';

      localStorage.setItem('auth_user_id', String(user.id));
      localStorage.setItem('auth_token', accessToken);
      localStorage.setItem('auth_user_name', user.name);
      localStorage.setItem('auth_user_email', user.email);
      localStorage.setItem('auth_user_picture_url', user.pictureUrl || '');
      localStorage.setItem('auth_user_dob', user.dateOfBirth || '');
      localStorage.setItem('auth_user_address', user.address || '');
      localStorage.setItem('auth_user_city', user.city || '');
      localStorage.setItem('auth_user_phone', user.phoneNumber || '');
    },
    updateProfileDetails: (state, action: PayloadAction<ProfileDetailsPayload>) => {
      state.userDob = action.payload.dob;
      state.userAddress = action.payload.address;
      state.userCity = action.payload.city;
      state.userPhone = action.payload.phone;

      localStorage.setItem('auth_user_dob', action.payload.dob);
      localStorage.setItem('auth_user_address', action.payload.address);
      localStorage.setItem('auth_user_city', action.payload.city);
      localStorage.setItem('auth_user_phone', action.payload.phone);
    },
    logout: (state) => {
      state.isLoggedIn = false;
      state.userId = null;
      state.token = null;
      state.userName = 'User';
      state.userEmail = 'Not available';
      state.userPictureUrl = null;
      state.userDob = '';
      state.userAddress = '';
      state.userCity = '';
      state.userPhone = '';

      localStorage.removeItem('auth_user_id');
      localStorage.removeItem('auth_token');
      localStorage.removeItem('auth_user_name');
      localStorage.removeItem('auth_user_email');
      localStorage.removeItem('auth_user_picture_url');
      localStorage.removeItem('auth_user_dob');
      localStorage.removeItem('auth_user_address');
      localStorage.removeItem('auth_user_city');
      localStorage.removeItem('auth_user_phone');
    }
  }
});

export const { loginSuccess, updateProfileDetails, logout } = authSlice.actions;
export default authSlice.reducer;
