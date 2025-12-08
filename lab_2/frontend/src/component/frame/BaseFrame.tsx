import type { ReactElement } from 'react';
import ReactDOM from 'react-dom';
import "../../style/Frames.css"


function BaseFrame({ children, isOpen, onClose, zindex, width, height}: {children: ReactElement, isOpen:boolean, onClose: () => void, zindex: number, width: string, height: string}) {
  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" style={{zIndex: zindex, width, height}} onClick={(e) => e.stopPropagation()}>
        {children}
      </div>
    </div>,
    document.getElementById('modal-root')!
  );
}

export default BaseFrame