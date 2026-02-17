import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type AuthState = {
  isLoggedIn: boolean;
  token: string | null;
  userName: string;
  userPictureUrl: string | null;
};

const initialToken = localStorage.getItem('auth_token');
const initialUserName = localStorage.getItem('auth_user_name');
const initialPictureUrl = localStorage.getItem('auth_user_picture_url');

const initialState: AuthState = {
  isLoggedIn: Boolean(initialToken),
  token: initialToken,
  userName: initialUserName || 'User',
  userPictureUrl: initialPictureUrl || null
};

type LoginPayload = {
  accessToken: string;
  user: {
    name: string;
    pictureUrl?: string | null;
  };
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess: (state, action: PayloadAction<LoginPayload>) => {
      const { accessToken, user } = action.payload;
      state.isLoggedIn = true;
      state.token = accessToken;
      state.userName = user.name;
      state.userPictureUrl = user.pictureUrl || null;

      localStorage.setItem('auth_token', accessToken);
      localStorage.setItem('auth_user_name', user.name);
      localStorage.setItem('auth_user_picture_url', user.pictureUrl || '');
    },
    logout: (state) => {
      state.isLoggedIn = false;
      state.token = null;
      state.userName = 'User';
      state.userPictureUrl = null;

      localStorage.removeItem('auth_token');
      localStorage.removeItem('auth_user_name');
      localStorage.removeItem('auth_user_picture_url');
    }
  }
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;
