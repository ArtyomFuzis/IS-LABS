import { useState, useEffect } from 'react'
import {fetchData} from '../Utils/apiQueries.ts'
import {makeQuerySelect } from '../Utils/baseMaps.ts';
import "../Styles/Table.css"
import type { SelectDTO } from '../interfaces/DTO/SelectDTO';
import LocationTable from './Tables/LocationTable';
import type { TableData } from '../interfaces/TableData';
import type { Location } from "../interfaces/Entities/Location";
import DisciplineTable from './Tables/DisciplineTable';
import type { Discipline } from '../interfaces/Entities/Discipline';
import CoordinateTable from './Tables/CoordinateTable';
import type { Coordinate } from '../interfaces/Entities/Coordinate';
import type { Person } from '../interfaces/Entities/Person';
import PersonTable from './Tables/PersonTable';
import LabWorkTable from './Tables/LabWorkTable';
import type { LabWork } from '../interfaces/Entities/LabWork';
import BaseFrame from './Frames/BaseFrame.tsx'
import CreateLocationSF from './Frames/SubFrames/CreateLocationSF.tsx'
import CreateCoordinateSF from './Frames/SubFrames/CreateCoordinateSF.tsx';
import CreateDisciplineSF from './Frames/SubFrames/CreateDisciplineSF.tsx';
import CreatePersonSF from './Frames/SubFrames/CreatePersonSF.tsx';
import CreateLabWorkSF from './Frames/SubFrames/CreateLabWorkSF.tsx';

function Table(params : {objectName: string}) {  

  const [data, setData] = useState({success: false});
  const [filterColumn, setFilterColumn] = useState("");
  const [filterData,   setFilterData]   = useState("");
  const [sortColumn, setSortColumn] = useState("");
  const [page, setPage] = useState(1);
  const [reversedSorting, setReversedSorting] = useState(false);
  const [nextPageExists,  setNextPageExists] = useState(false);
  const [modalOpen, setModalOpen] = useState("")
  const [chosenRow, setChosenRow] = useState(-1)
  

  const loadData = async () => {
      const result = await fetchData(makeQuerySelect(params.objectName,filterColumn,filterData,sortColumn,page,reversedSorting) );
      setData(result);
      const result_next : SelectDTO<any> = await fetchData(makeQuerySelect(params.objectName,filterColumn,filterData,sortColumn,page+1,reversedSorting) );
      setNextPageExists(result_next.result.length !== 0);
      transfered_data = {data: (data as SelectDTO<any>), filterColumn, filterData, sortColumn, reversedSorting, setFilterColumn, setFilterData, setSortColumn, setReversedSorting, modalOpen, setModalOpen}
    };

  let transfered_data : TableData<any> = {data: (data as SelectDTO<any>), filterColumn, filterData, sortColumn, reversedSorting, setFilterColumn, setFilterData, setSortColumn, setReversedSorting, modalOpen, setModalOpen}

  useEffect(() => {
    loadData();    
    const interval = setInterval(loadData, 10000);
    return () => clearInterval(interval);
  }, [page, filterColumn, filterData, sortColumn, reversedSorting, modalOpen]);

  function modalOnClose(){
    setModalOpen("")
    loadData()   
  }

  function getByChosen(){
    let result:any = null
    transfered_data.data.result.forEach(function(el) {
      if (el.id == chosenRow) result = el
    });
    return result 
  }

  return (
    <>
      <BaseFrame isOpen={modalOpen != ""} onClose={modalOnClose} zindex={1000} width="40%" height=''>
        <>
          {modalOpen=="LocationCreate" && <CreateLocationSF onClose={modalOnClose} id={-1} location={undefined}/>}
          {modalOpen=="CoordinateCreate" && <CreateCoordinateSF onClose={modalOnClose} id={-1} coordinate={undefined}/>}
          {modalOpen=="DisciplineCreate" && <CreateDisciplineSF onClose={modalOnClose} id={-1} discipline={undefined}/>}
          {modalOpen=="PersonCreate" && <CreatePersonSF onClose={modalOnClose} id={-1} person={undefined}/>}
          {modalOpen=="LabWorkCreate" && <CreateLabWorkSF onClose={modalOnClose} id={-1} labWork={undefined}/>}
          {modalOpen=="ModifyLocation" && <CreateLocationSF onClose={modalOnClose} id={chosenRow} location={getByChosen()}/>}
          {modalOpen=="ModifyDiscipline" && <CreateDisciplineSF onClose={modalOnClose} id={chosenRow} discipline={getByChosen()}/>}
          {modalOpen=="ModifyCoordinate" && <CreateCoordinateSF onClose={modalOnClose} id={chosenRow} coordinate={getByChosen()}/>}
          {modalOpen=="ModifyPerson" && <CreatePersonSF onClose={modalOnClose} id={chosenRow} person={getByChosen()}/>}
          {modalOpen=="ModifyLabWork" && <CreateLabWorkSF onClose={modalOnClose} id={chosenRow} labWork={getByChosen()}/>}
        </>
      </BaseFrame>
      <div className='table-container'>
        <div className='table-controllers'>
          <div className='menu-button-container table-previous-button'>
            <button className={`menu-button ${page === 1 ? "table-button-inactive" : ""}`} onClick={()=>setPage(page-1)}>
                Прошлая страница
            </button>
          </div>
          <div className='menu-button-container table-next-button'>
            <button className={`menu-button ${!nextPageExists ? "table-button-inactive" : ""}`} onClick={()=>{setPage(page+1)}}>
              Следующая страница
            </button>
          </div>
        </div>
        {transfered_data !== null && data.success &&
          <div className='table-body'>
            {params.objectName == 'Location' && <LocationTable tableData={{...transfered_data, data: (data as SelectDTO<Location>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={true}/>}
            {params.objectName == 'Discipline' && <DisciplineTable tableData={{...transfered_data, data: (data as SelectDTO<Discipline>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={true}/>}
            {params.objectName == 'Coordinate' && <CoordinateTable tableData={{...transfered_data, data: (data as SelectDTO<Coordinate>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={true}/>}
            {params.objectName == 'Person' && <PersonTable tableData={{...transfered_data, data: (data as SelectDTO<Person>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={true}/>}
            {params.objectName == 'LabWork' && <LabWorkTable tableData={{...transfered_data, data: (data as SelectDTO<LabWork>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={true}/>}
            <div className='main-table-add-button' onClick={() => setModalOpen(params.objectName+"Create")}>Добавить объект</div>
          </div>
        }
      </div>
    </>
    
  );
};

export default Table