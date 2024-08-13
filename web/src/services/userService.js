const url = process.env.NEXT_PUBLIC_BACKEND_URL;
const baseUrl = `${url}/api/users`;

const getFetchOptions = (method, body) => ({
  method,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${localStorage.getItem('token')}`,
  },
  body: body ? JSON.stringify(body) : undefined,
});

const handleResponse = async (response) => {
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  return await response.json();
};

const userService = {
  getUser: async (id) => {
    const response = await fetch(`${baseUrl}/${id}`, getFetchOptions('GET'));
    return handleResponse(response);
  },

  getUsers: async () => {
    try {
      const response = await fetch(baseUrl, getFetchOptions('GET'));
      return handleResponse(response);
    } catch (error) {
      console.error('There was a problem with the fetch operation: ', error);
      throw error;
    }
  },

  createUser: async (user) => {
    const response = await fetch(baseUrl, getFetchOptions('POST', user));
    return handleResponse(response);
  },

  updateUser: async (user, id) => {
    const response = await fetch(
      `${baseUrl}/${id}`,
      getFetchOptions('PUT', user)
    );
    return handleResponse(response);
  },

  deleteUser: async (id) => {
    const response = await fetch(`${baseUrl}/${id}`, getFetchOptions('DELETE'));
    return handleResponse(response);
  },

  updateAdmin: async (id) => {
    const response = await fetch(
      `${baseUrl}/admin/${id}`,
      getFetchOptions('PUT')
    );
    return handleResponse(response);
  },

  updateProfileImage: async (id, image) => {
    const formData = new FormData();
    formData.append('image', image);
    try {
      const response = await fetch(`${baseUrl}/image/${id}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: formData,
      });
      if (!response.ok) {
        const text = await response.text();
        throw new Error(
          `HTTP error! status: ${response.status}, Body: ${text}`
        );
      }

      return await response.json();
    } catch (error) {
      console.error('Error during the fetch operation:', error);
      throw error;
    }
  },

  updatePassword: async (id, password) => {
    try {
      const response = await fetch(
        `${baseUrl}/password/${id}`,
        getFetchOptions('PUT', password)
      );
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error(
        `There was a problem with the update password operation: ${error}`
      );
      throw error;
    }
  },
};

export default userService;
