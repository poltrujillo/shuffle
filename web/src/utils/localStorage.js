'use client';

/**
 * Saves a string to localStorage safely.
 * @param {string} key - The key under which to store the item.
 * @param {string} value - The string value to store.
 */
const saveUniqueItem = (key, value) => {
  try {
    // Directly save the string value
    localStorage.setItem(key, value);
  } catch (error) {
    console.error('Failed to save to localStorage:', error);
    // Possible actions here could include retrying the save or notifying the user
  }
};

/**
 * Retrieves a string value from localStorage.
 * @param {string} key - The key of the item to retrieve.
 * @returns {string|null} The string value from localStorage or null if not found or in case of an error.
 */
const getItem = (key) => {
  try {
    const item = localStorage.getItem(key);
    return item; // Return the direct string from localStorage
  } catch (error) {
    console.error('Failed to retrieve from localStorage:', error);
    return null; // Return null if there's an error
  }
};

/**
 * Removes an item from localStorage.
 * @param {string} key - The key of the item to remove.
 */
const removeItem = (key) => {
  try {
    localStorage.removeItem(key);
  } catch (error) {
    console.error('Failed to remove item from localStorage:', error);
    // Consider whether to handle failures e.g., by retrying
  }
};

export { saveUniqueItem, getItem, removeItem };
