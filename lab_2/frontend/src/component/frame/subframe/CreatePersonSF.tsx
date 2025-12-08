import { useEffect, useState } from "react"
import ParameterField from "../frame-component/ParameterField"
import { processIsNotNull, processStringMaxLength, processStringMinLength } from "../../../util/validations"
import ErrorPanel from "../frame-component/ErrorPanel"
import { makeQueryCreate, makeQueryDelete } from "../../../util/baseMaps"
import { createObject, removeObject } from "../../../util/apiQueries"
import type { Person } from "../../../interface/entity/Person"
import BaseFrame from "../BaseFrame"
import DeleteAlert from "./DeleteAlert"
import ShowSF from "./ShowSF"

function CreatePersonSF({ onClose, id, person }: { onClose: () => void, id: number, person: Person|undefined}) {
    const [name, setName] : [any, React.Dispatch<any>] = useState("")
    const [eyeColor, setEyeColor] : [any, React.Dispatch<any>] = useState("")
    const [hairColor, setHairColor] : [any, React.Dispatch<any>] = useState("")
    const [passportId, setPassportId] : [any, React.Dispatch<any>] = useState("")
    const [nationality, setNationality] : [any, React.Dispatch<any>] = useState("")
    const [location, setLocation] : [any, React.Dispatch<any>] = useState(-1)
    
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    const [alert, setAlert] = useState(false)
    const [modalOpen, setModalOpen] = useState("")
    function showConnected(){
        setModalOpen("ShowLabWork")
    }
    function modalOnClose(){
        setModalOpen("")
    }
    useEffect(() => {
        if(person != undefined){
            setName(person.name)
            setEyeColor(person.eyeColor?.id) 
            if(person.hairColor == null) setHairColor("")
            else setHairColor(person.hairColor?.id)
            if(person.passportId == null) setPassportId("")
            else setPassportId(person.passportId)
            if(person.nationality == null) setNationality("")
            else setNationality(person.nationality?.id)
            if(person.location == null)setLocation(-1)
            else setLocation(person.location?.id )
        }
    },[])

    useEffect(() => {
        let res : string[] = []
        processStringMaxLength(name, 400, "Имя", res)
        processIsNotNull(name, "Имя", res)
        processStringMinLength(passportId, 6, "Паспорт", res)
        processStringMaxLength(passportId, 50, "Паспорт", res)
        setErrorMsg(res)
    }, [name, eyeColor, hairColor, passportId, nationality]);

    function createQuery(){
        let form = new URLSearchParams()
        if(id != -1) form.append('id', id.toString())
        form.append('name', name)
        form.append('eye_color_id', eyeColor)
        if(hairColor != "") form.append('hair_color_id', hairColor)
        if(location != -1) form.append('location_id', location.toString())
        if(passportId != "") form.append('passport_id', passportId)
        if(nationality != "") form.append('nationality_id', nationality)
        createObject(makeQueryCreate("Person"),form)
        onClose()
    }

    function deleteQuery(){
        removeObject(makeQueryDelete("Person", id.toString()))
        .then(() =>onClose())
        .catch(() => {
            setAlert(true)
        })
    }

    return (
        <>
            <BaseFrame isOpen={alert} onClose={() => setAlert(false)} zindex={5000} width="30%" height="35%">
                <>
                    <DeleteAlert onClose={() => setAlert(false)}></DeleteAlert>
                </>
            </BaseFrame>
            <BaseFrame isOpen={modalOpen != ""} onClose={modalOnClose} zindex={1000} width="40%" height="">
                <>
                    {modalOpen=="ShowLabWork" && <ShowSF onClose={modalOnClose} objectName="LabWork" filterColumnEx="authorId" filterDataEx={id.toString()}/>}
                </>
            </BaseFrame>
            <div className="modal-main-content">
              <div className="modal-header">{id == -1 ? "Создание" : "Изменение"} Person</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={eyeColor} setValue={setEyeColor} required={true} type="enumSelectcolor" field="Цвет глаз" />
                    <ParameterField value={hairColor} setValue={setHairColor} required={false} type="enumSelectcolor" field="Цвет волос" />
                    <ParameterField value={passportId} setValue={setPassportId} required={false} type="text" field="Паспорт" />
                    <ParameterField value={nationality} setValue={setNationality} required={false} type="enumSelectcountry" field="Национальность" />
                    <ParameterField value={location} setValue={setLocation} required={false} type="chooseLocation" field="Адрес" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                {
                    person != undefined && 
                    <button className={`modal-save-button`} onClick={showConnected}>
                        Связанные
                    </button>
                }
                {
                    person != undefined && 
                    <button className={`modal-save-button`} onClick={deleteQuery}>
                        Удалить
                    </button>
                }
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    {id == -1 ? "Создать" : "Изменить"}
                </button>
            </div>
        </>
    )
}

export default CreatePersonSF