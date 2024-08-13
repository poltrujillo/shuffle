import {
  collection,
  getDocs,
  addDoc,
  doc,
  updateDoc,
  getDoc,
} from 'firebase/firestore';
import { db } from '@/utils/firebase';

const rankingService = {
  addRanking: async (email, username, score) => {
    try {
      const docRef = await addDoc(collection(db, 'rankings'), {
        email,
        username,
        score,
      });
      return docRef.id;
    } catch (error) {
      console.error('Error adding document: ', error);
      throw error;
    }
  },

  updateRanking: async (id, score) => {
    try {
      await updateDoc(doc(db, 'rankings', id), {
        score,
      });
    } catch (error) {
      console.error('Error updating document: ', error);
      throw error;
    }
  },

  getRankings: async () => {
    try {
      const querySnapshot = await getDocs(collection(db, 'rankings'));
      const rankings = [];
      querySnapshot.forEach((doc) => {
        rankings.push({ id: doc.id, ...doc.data() });
      });
      return rankings.sort((a, b) => b.score - a.score);
    } catch (error) {
      console.error('Error getting documents: ', error);
      throw error;
    }
  },

  getRankingById: async (id) => {
    try {
      const docRef = await getDoc(doc(db, 'rankings', id));
      if (docRef.exists()) {
        return { id: docRef.id, ...docRef.data() };
      }
      return null;
    } catch (error) {
      console.error('Error getting document: ', error);
      throw error;
    }
  },
};

export default rankingService;
