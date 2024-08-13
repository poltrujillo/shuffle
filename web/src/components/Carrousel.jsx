'use client';

import React from 'react';
import { cn } from '@/utils/cn';
import { Button } from '@nextui-org/react';
import Link from 'next/link';
import Image from 'next/image';

class Carrousel extends React.Component {
  containerRef = React.createRef();
  scrollerRef = React.createRef();

  constructor(props) {
    super(props);
    this.state = {
      start: false,
    };
  }

  componentDidMount() {
    this.addAnimation();
  }

  addAnimation = () => {
    if (this.containerRef.current && this.scrollerRef.current) {
      const scrollerContent = Array.from(this.scrollerRef.current.children);

      scrollerContent.forEach((item) => {
        const duplicatedItem = item.cloneNode(true);
        if (this.scrollerRef.current) {
          this.scrollerRef.current.appendChild(duplicatedItem);
        }
      });

      this.getDirection();
      this.getSpeed();
      this.setState({ start: true });
    }
  };

  getDirection = () => {
    const { direction } = this.props;
    if (this.containerRef.current) {
      if (direction === 'left') {
        this.containerRef.current.style.setProperty(
          '--animation-direction',
          'forwards'
        );
      } else {
        this.containerRef.current.style.setProperty(
          '--animation-direction',
          'reverse'
        );
      }
    }
  };

  getSpeed = () => {
    const { speed } = this.props;
    if (this.containerRef.current) {
      if (speed === 'fast') {
        this.containerRef.current.style.setProperty(
          '--animation-duration',
          '20s'
        );
      } else if (speed === 'normal') {
        this.containerRef.current.style.setProperty(
          '--animation-duration',
          '40s'
        );
      } else {
        this.containerRef.current.style.setProperty(
          '--animation-duration',
          '80s'
        );
      }
    }
  };

  render() {
    const { items, pauseOnHover, className } = this.props;
    const { start } = this.state;

    return (
      <div
        ref={this.containerRef}
        className={cn(
          'scroller relative z-20 max-w-7xl overflow-hidden [mask-image:linear-gradient(to_right,transparent,white_20%,white_80%,transparent)]',
          className
        )}
      >
        <ul
          ref={this.scrollerRef}
          className={cn(
            'flex min-w-full shrink-0 gap-4 py-4 w-max flex-nowrap',
            start && 'animate-scroll',
            pauseOnHover && 'hover:[animation-play-state:paused]'
          )}
        >
          {items.map((item, idx) => (
            <li
              className="w-[150px] max-w-full relative rounded-2xl border border-b-0 flex-shrink-0 border-slate-700 px-8 py-6 md:w-[450px]"
              style={{
                background:
                  'linear-gradient(180deg, var(--slate-800), var(--slate-900)',
              }}
              key={item.name}
            >
              <blockquote>
                <div
                  aria-hidden="true"
                  className="user-select-none -z-1 pointer-events-none absolute -left-0.5 -top-0.5 h-[calc(100% + 4px)] w-[calc(100% + 4px)]"
                ></div>
                <span className="relative z-20 text-sm leading-[1.6] text-gray-100 font-normal">
                  {item.quote}
                </span>
                <div className="relative z-20 mt-6 flex flex-row items-center justify-evenly">
                  <span className="flex flex-col gap-1">
                    <span className="text-sm leading-[1.6] text-gray-400 font-normal">
                      {item.name}
                    </span>
                    <span className="text-sm leading-[1.6] text-gray-400 font-normal">
                      <Button
                        color="primary"
                        variant="bordered"
                        size="sm"
                        className="hover:bg-shuffle-primary-50"
                      >
                        <Link href={item.link}>Select</Link>
                      </Button>
                    </span>
                  </span>
                  <Image
                    src={item.src}
                    alt={item.title}
                    height={250}
                    width={250}
                    className="rounded-2xl opacity-70"
                  ></Image>
                </div>
              </blockquote>
            </li>
          ))}
        </ul>
      </div>
    );
  }
}

export default Carrousel;
