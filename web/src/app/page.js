import MainContent from '@/components/MainContent';
import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';

export default async function Page() {
  return (
    <>
      <Navbar />
      <div className="h-screen w-screen flex flex-col justify-center items-center gap-14">
        <MainContent />
        <Footer />
      </div>
    </>
  );
}
