'use client';

import rankingService from '@/services/rankingService';
import React, { useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import { ScaleLoader } from 'react-spinners';

const Ranking = () => {
  const [rankingList, setRankingList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [rankingsPerPage] = useState(10);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRankings = async () => {
      const fetchedRankings = await rankingService.getRankings();
      setRankingList(fetchedRankings);
      setLoading(false);
    };
    fetchRankings();
  }, []);

  const indexOfLastRanking = currentPage * rankingsPerPage;
  const indexOfFirstRanking = indexOfLastRanking - rankingsPerPage;
  const currentRankings = rankingList.slice(
    indexOfFirstRanking,
    indexOfLastRanking
  );

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const showUserInfo = (e) => {
    const username = e.target.parentNode.children[2].innerText;
    const score = e.target.parentNode.children[1].innerText;
    Swal.fire({
      title: username,
      text: `Score: ${score}`,
      imageUrl: '/img/main/leaderboard.webp',
      imageWidth: 400,
      imageHeight: 200,
      imageAlt: 'User info position image',
    });
  };

  if (loading) {
    return (
      <div className="h-screen flex justify-center items-center bg-gray-800">
        <ScaleLoader color="#9F7AEA" />{' '}
      </div>
    );
  }

  return (
    <div className="text-gray-300 p-5 w-screen h-screen flex flex-col justify-center items-center">
      <h1 className="text-3xl font-bold text-white mb-4">Rankings</h1>
      <div className="overflow-hidden rounded-lg shadow-lg bg-shuffle-quinary p-3 w-2/3">
        <table className="w-full">
          <thead>
            <tr className="text-white text-left">
              <th className="py-2 px-4">Position</th>
              <th className="py-2 px-4">Score</th>
              <th className="py-2 px-4">Username</th>
            </tr>
          </thead>
          <tbody className="divide-y">
            {currentRankings.map((ranking, index) => (
              <tr
                key={ranking.id}
                onClick={showUserInfo}
                className="transition-colors cursor-pointer hover:bg-purple-800"
              >
                <td className="py-2 px-4">{indexOfFirstRanking + index + 1}</td>
                <td className="py-2 px-4">{ranking.score}</td>
                <td className="py-2 px-4">{ranking.username}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Pagination
        rankingsPerPage={rankingsPerPage}
        totalRankings={rankingList.length}
        paginate={paginate}
      />
    </div>
  );
};

const Pagination = ({ rankingsPerPage, totalRankings, paginate }) => {
  const pageNumbers = [];

  for (let i = 1; i <= Math.ceil(totalRankings / rankingsPerPage); i++) {
    pageNumbers.push(i);
  }

  return (
    <nav>
      <ul className="flex flex-row gap-3 justify-center mt-4">
        {pageNumbers.map((number) => (
          <li key={number} className="rounded-full">
            <a
              onClick={() => paginate(number)}
              href="#!"
              className="page-link block w-10 h-10 leading-8 text-center bg-purple-700 hover:bg-purple-800 text-white transition-colors rounded-xl text-xl font-bold pt-1"
            >
              {number}
            </a>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default Ranking;
