import headerData from '../header.json'

export default function Header({ id }) {
    
    if(id.length == 0) {
        return (
            <div className='flex flex-col justify-center px-20 py-28 hero-bg'>
                <h1 className="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
                    No report selected
                </h1>
                <p className="mt-3 text-base text-gray-500 sm:mt-5 sm:text-lg sm:max-w-xl sm:mx-auto md:mt-5 md:text-xl lg:mx-0">To get started use the dropdown menu bellow to select what report you would like to generate.</p>
                <p className="mt-3 text-base text-gray-500 sm:mt-5 sm:text-lg sm:max-w-xl sm:mx-auto md:mt-5 md:text-xl lg:mx-0">Note that some reports require an additional user input.</p>
            </div>
        )
    } else {
        return (
            <>
                {headerData.map(header => {
                    if(header.id == id) {
                        return (
                            <div className='flex flex-col justify-center px-20 py-28 hero-bg'>
                                <h1 className="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
                                    <span className="block xl:inline">{header.reportName}</span>{' '}
                                    <span className="block text-indigo-600">Id - {header.id}</span>
                                </h1>
                                <p className="mt-3 text-base text-gray-500 sm:mt-5 sm:text-lg sm:max-w-xl sm:mx-auto md:mt-5 md:text-xl lg:mx-0">{header.description}</p>
                            </div>
                        )
                    }
                })}
            </>
        )
    }
}