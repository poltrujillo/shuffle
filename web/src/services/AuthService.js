import { getItem, removeItem } from '@/utils/localStorage';

const url = process.env.NEXT_PUBLIC_BACKEND_URL;

class AuthService {
  constructor(baseUrl = `${url}/api`) {
    this.url = baseUrl;
  }

  async fetchWithAuth(endpoint, options) {
    const response = await fetch(`${this.url}${endpoint}`, options);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Request failed');
    }
    return response.json();
  }

  async login(username, password) {
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: username, password }),
    };
    return this.fetchWithAuth('/login', options);
  }

  async register(username, email, password) {
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password }),
    };
    const data = await this.fetchWithAuth('/register', options);
    return { token: data.token, user: await this.getUser(data.token) };
  }

  async logout() {
    const token = getItem('token');
    const options = {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    };
    await this.fetchWithAuth('/logout', options);
    removeItem('token');
  }

  async getUser(token) {
    const options = {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    };
    return this.fetchWithAuth('/auth/user', options);
  }
}

export default AuthService;
